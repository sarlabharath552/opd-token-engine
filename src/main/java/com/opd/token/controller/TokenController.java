package com.opd.token.controller;

import org.springframework.web.bind.annotation.*;

import com.opd.token.domain.entity.Patient;
import com.opd.token.domain.entity.Token;
import com.opd.token.domain.enums.Priority;
import com.opd.token.service.TokenAllocationService;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenAllocationService tokenService;

    public TokenController(TokenAllocationService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/book")
    public Token bookToken(@RequestParam Long slotId,
                           @RequestParam Priority priority,
                           @RequestBody Patient patient) {
        return tokenService.bookToken(slotId, patient, priority);
    }

    @PostMapping("/emergency")
    public Token emergency(@RequestParam Long slotId,
                           @RequestBody Patient patient) {
        return tokenService.emergencyBook(slotId, patient);
    }

    @DeleteMapping("/{tokenId}")
    public String cancel(@PathVariable Long tokenId) {
        tokenService.cancelToken(tokenId);
        return "Token cancelled successfully";
    }

    @PostMapping("/{tokenId}/no-show")
    public String noShow(@PathVariable Long tokenId) {
        tokenService.markNoShow(tokenId);
        return "Token marked as NO-SHOW";
    }
}
