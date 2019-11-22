package com.filter;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.util.DateTimeUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class TokenHandler {

	public static final String PREFIX 			= 	"Bearer ";
	public static final String TOKEN_EXPIRES 	= 	"Token expires";
	private static final String SECRET 			= 	"JWT_TOKEN_SECRET";
	private static final byte[] SECRET_ARRAY 	= 	SECRET.getBytes();
	private static final long EXPIRATION_TIME 	= 	DateTimeUtils.ONE_MINUTE;

	/**
	 * Build token from username
	 * @param username is username
	 * @return jwt
	 */
	public static String buildToken(String username) {
		if (StringUtils.isEmpty(username)) {
			throw new IllegalArgumentException("Can't create JWT token without username!");
		}
		return Jwts.builder() //
				.setSubject(username) //
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //
				.signWith(SignatureAlgorithm.HS512, SECRET_ARRAY) //
				.compact();
	}

	/**
	 * Get username from token
	 * @param token jwt token
	 * @return username
	 */
	public static String getUsername(String token) {
		try {
			// if (StringUtils.isEmpty(token)) { return StringUtils.EMPTY; }
			return Jwts.parser() //
					.setSigningKey(SECRET_ARRAY) //
					.parseClaimsJws(token.replace(PREFIX, StringUtils.EMPTY)) //
					.getBody() //
					.getSubject();
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | ExpiredJwtException
				| IllegalArgumentException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}
	
}
