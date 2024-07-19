package com.example.crud.auth;

import com.example.crud.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Login failed"));
        }
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Registration failed"));
        }
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<Map<String, Object>> verifyToken(HttpServletRequest request) {
        // Extrae el token del encabezado Authorization
        String token = extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (token != null && jwtService.isTokenValid(token, userDetailsService.loadUserByUsername(jwtService.getUsernameFromToken(token)))) {
            // Extrae el nombre de usuario del token
            String username = jwtService.getUsernameFromToken(token);

            try {
                // Carga los detalles del usuario y obtiene los roles
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                // Mapea los roles a una lista de strings
                List<String> roles = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                // Prepara la respuesta con los roles
                Map<String, Object> response = new HashMap<>();
                response.put("roles", roles);

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Could not load user details"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid or expired token"));
        }
    }

    private String extractTokenFromHeader(String header) {
        // Asegúrate de que el encabezado tiene el formato esperado
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
