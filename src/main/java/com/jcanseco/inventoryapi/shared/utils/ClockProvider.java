package com.jcanseco.inventoryapi.shared.utils;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ClockProvider {
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
