package com.example.legacy.legacyService;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.legacy.DTOs.LegacyValidationResponse;
import com.example.legacy.support.DeadLetterItem;
import com.example.legacy.support.DeadLetterQueue;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Legacy Service")
@RestController
@RequestMapping("/legacy")
public class LegacyController {

        private static final Logger log = LoggerFactory.getLogger(LegacyController.class);

        private final Random random = new Random();

        @ApiOperation("Validates legacy system (intentionally unstable)")
        @PostMapping("/validate")
        public ResponseEntity<LegacyValidationResponse> validate(
                        @RequestBody(required = false) Object request)
                        throws InterruptedException {

                log.info("Legacy validation request received");

                try {
                        int decision = random.nextInt(3);

                        if (decision == 0) {
                                log.warn("Legacy system responding slowly");
                                Thread.sleep(3000);

                                log.info("Legacy validation OK (slow)");
                                return ResponseEntity.ok(
                                                new LegacyValidationResponse(
                                                                "OK",
                                                                "Legacy validation OK (slow)"));
                        }

                        if (decision == 1) {
                                log.error("Legacy system internal error");

                                DeadLetterQueue.add(
                                                new DeadLetterItem(
                                                                extractOperationId(request),
                                                                "LEGACY_INTERNAL_ERROR"));

                                return ResponseEntity
                                                .status(HttpStatus.FAILED_DEPENDENCY)
                                                .body(
                                                                new LegacyValidationResponse(
                                                                                "ERROR",
                                                                                "Legacy system error"));
                        }

                        log.info("Legacy validation OK");
                        return ResponseEntity.ok(
                                        new LegacyValidationResponse(
                                                        "OK",
                                                        "Legacy validation OK"));

                } catch (Exception ex) {

                        log.error("Unexpected legacy error", ex);

                        DeadLetterQueue.add(
                                        new DeadLetterItem(
                                                        extractOperationId(request),
                                                        ex.getMessage()));

                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(
                                                        new LegacyValidationResponse(
                                                                        "ERROR",
                                                                        "Unexpected legacy error"));
                }
        }

        private String extractOperationId(Object request) {
                try {
                        if (request == null) {
                                return "UNKNOWN";
                        }
                        return request.toString();
                } catch (Exception e) {
                        return "UNKNOWN";
                }
        }

        // Endpoint interno de soporte
        @GetMapping("/dead-letter")
        public Object getDeadLetters() {
                return DeadLetterQueue.getAll();
        }
}
