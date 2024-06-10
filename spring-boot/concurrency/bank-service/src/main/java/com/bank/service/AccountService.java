package com.bank.service;

import com.bank.dto.request.AtmRequest;
import com.bank.dto.response.AtmResponse;
import com.bank.repository.AccountRepository;
import com.bank.repository.AccountVersionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountVersionRepository accountVersionRepository;


    @Transactional
    public AtmResponse withdraw(AtmRequest request) {
        String username = request.getUsername();

        AtmResponse response = new AtmResponse();
        response.setUsername(username);

        // => PESSIMISTIC_WRITE
        var accountOpt = accountRepository.findByUsername(username);

        if (accountOpt.isEmpty()) {
            response.setMessage("Account not found");
            return response;
        }

        var account = accountOpt.get();

        if (account.getBalance() < request.getAmount()) {
            response.setMessage("Insufficient balance");
            return response;
        }

        account.setBalance(account.getBalance() - request.getAmount());

        var entity = accountRepository.save(account);

        response.setStatus(true);
        response.setMessage("Successfully");
        response.setAccountBalance(entity.getBalance());
        response.setAmountWithdrawn(request.getAmount());

        return response;
    }

    @Transactional
    public AtmResponse withdrawVersion(AtmRequest request) {
        String username = request.getUsername();

        AtmResponse response = new AtmResponse();
        response.setUsername(username);

        // => OPTIMISTIC_FORCE_INCREMENT + @Version
        var accountOpt = accountVersionRepository.findByUsername(username);

        if (accountOpt.isEmpty()) {
            response.setMessage("Account not found");
            return response;
        }

        var account = accountOpt.get();

        if (account.getBalance() < request.getAmount()) {
            response.setMessage("Insufficient balance");
            return response;
        }

        account.setBalance(account.getBalance() - request.getAmount());

        var entity = accountVersionRepository.save(account);

        response.setStatus(true);
        response.setMessage("Successfully");
        response.setAccountBalance(entity.getBalance());
        response.setAmountWithdrawn(request.getAmount());

        return response;
    }

}
