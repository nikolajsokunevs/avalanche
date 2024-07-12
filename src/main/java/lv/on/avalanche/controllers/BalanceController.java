package lv.on.avalanche.controllers;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.BalanceDTO;
import lv.on.avalanche.services.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/get/{userId}")
    public BalanceDTO getByUserId(@PathVariable Long userId) {
        return balanceService.getBalance(userId);
    }

    @PostMapping("/add/balance/{userId}/{amount}")
    public BalanceDTO addBalance(@PathVariable Long userId, @PathVariable String amount) {
        return balanceService.addBalance(userId, amount);
    }
}
