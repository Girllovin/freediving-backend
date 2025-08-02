package smallITgroup.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    // TODO: замени на более безопасный ключ и храни его в application.properties или как секрет
    private final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret";

    // Время жизни токена (например, 24 часа)
    private final long EXPIRATION_MS = 1000 * 60 * 60 * 24;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Генерация JWT по email
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Извлечение email (subject) из токена
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Проверка, не истёк ли срок действия токена
     */
    public boolean isTokenValid(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.after(new Date());
    }

    /**
     * Получение всех Claims из токена
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
