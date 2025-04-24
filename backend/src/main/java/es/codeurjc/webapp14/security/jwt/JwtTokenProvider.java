package es.codeurjc.webapp14.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

	private final SecretKey jwtSecret = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
	private final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecret).build();

	public String tokenStringFromHeaders(HttpServletRequest req) {
		String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (bearerToken == null) {
			throw new IllegalArgumentException("Missing Authorization header");
		}
		if (!bearerToken.startsWith("Bearer ")) {
			throw new IllegalArgumentException("Authorization header does not start with Bearer: " + bearerToken);
		}
		return bearerToken.substring(7);
	}

	public String tokenStringFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (TokenType.ACCESS.cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new IllegalArgumentException("No access token cookie found in request");
	}

	public Claims validateToken(HttpServletRequest req, boolean fromCookie) {
		var token = fromCookie ? tokenStringFromCookies(req) : tokenStringFromHeaders(req);
		return validateToken(token);
	}

	public Claims validateToken(String token) {
		return jwtParser.parseClaimsJws(token).getBody();
	}

	public String generateAccessToken(UserDetails userDetails) {
		return buildToken(TokenType.ACCESS, userDetails).compact();
	}

	public String generateRefreshToken(UserDetails userDetails) {
		var token = buildToken(TokenType.REFRESH, userDetails);
		return token.compact();
	}

	private JwtBuilder buildToken(TokenType tokenType, UserDetails userDetails) {
		var currentDate = new Date();
		var expiryDate = Date.from(new Date().toInstant().plus(tokenType.duration));
		return Jwts.builder()
				.claim("roles", userDetails.getAuthorities())
				.claim("type", tokenType.name())
				.setSubject(userDetails.getUsername())
				.setIssuedAt(currentDate)
				.setExpiration(expiryDate)
				.signWith(jwtSecret);
	}
}
