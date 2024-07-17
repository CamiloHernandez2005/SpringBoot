package com.example.crud.auth;

import com.example.crud.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(HttpServletRequest request) {
        // Extrae el token del encabezado Authorization
        String token = extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        // Verifica si el token es válido
        if (token != null) {
            String username = jwtService.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (userDetails != null && jwtService.isTokenValid(token, userDetails)) {
                return ResponseEntity.ok("Token is valid");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is not valid or expired");
    }

    // Método para extraer el token del encabezado Authorization
    private String extractTokenFromHeader(String header) {
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


}
