package lv.on.avalanche.services;

import lombok.extern.slf4j.Slf4j;
import lv.on.avalanche.dto.BalanceDTO;
import lv.on.avalanche.entities.BalanceEntity;
import lv.on.avalanche.mapper.BalanceMapper;
import lv.on.avalanche.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceMapper balanceMapper;

    public BalanceDTO getBalance(Long userId){
        return balanceMapper.toDTO(balanceRepository.findByUserId(userId));
    }

    public BalanceDTO addBalance(Long userId, String amount){
        log.info("Add balance, user: {}, amount: {}", userId, amount);
        BalanceEntity balance=balanceRepository.findByUserId(userId);
        balance.setBalance(balance.getBalance()+Double.valueOf(amount));
        balanceRepository.save(balance);
        return balanceMapper.toDTO(balance);
    }

    public BalanceDTO withdrawal(Long userId){
        BalanceEntity balance=balanceRepository.findByUserId(userId);
        balance.setBalance(0.00);
        balanceRepository.save(balance);
        return balanceMapper.toDTO(balance);
    }
}
