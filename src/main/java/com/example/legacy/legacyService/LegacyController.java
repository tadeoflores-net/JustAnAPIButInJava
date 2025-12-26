package com.example.legacy.legacyService;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.legacy.DTOs.LegacyValidationResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Legacy Service")
@RestController
@RequestMapping("/legacy")
public class LegacyController {

    private static final Logger log =
            LoggerFactory.getLogger(LegacyController.class);

    private final Random random = new Random();

    @ApiOperation("Validates legacy system (intentionally unstable)")
    @PostMapping("/validate")
    public ResponseEntity<LegacyValidationResponse> validate()
            throws InterruptedException {

        log.info("Legacy validation request received");

        int decision = random.nextInt(3);

        // Respuesta lenta
        if (decision == 0) {
            log.warn("Legacy system responding slowly");
            Thread.sleep(3000);

            log.info("Legacy validation OK (slow)");
            return ResponseEntity.ok(
                    new LegacyValidationResponse(
                            "OK",
                            "Legacy validation OK (slow)"
                    )
            );
        }

        // Error del sistema
        if (decision == 1) {
            log.error("Legacy system internal error");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new LegacyValidationResponse(
                                    "ERROR",
                                    "Legacy system error"
                            )
                    );
        }

        // Respuesta exitosa
        log.info("Legacy validation OK");
        return ResponseEntity.ok(
                new LegacyValidationResponse(
                        "OK",
                        "Legacy validation OK"
                )
        );
    }
}
