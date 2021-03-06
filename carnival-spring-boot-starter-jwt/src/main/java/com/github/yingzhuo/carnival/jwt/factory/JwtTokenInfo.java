/*
 *  ____    _    ____  _   _ _____     ___    _
 * / ___|  / \  |  _ \| \ | |_ _\ \   / / \  | |
 * | |    / _ \ | |_) |  \| || | \ \ / / _ \ | |
 * | |___/ ___ \|  _ <| |\  || |  \ V / ___ \| |___
 * \____/_/   \_\_| \_\_| \_|___|  \_/_/   \_\_____|
 *
 * https://github.com/yingzhuo/carnival
 */
package com.github.yingzhuo.carnival.jwt.factory;

import com.github.yingzhuo.carnival.jwt.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author 应卓
 */
@Getter
@Setter
public class JwtTokenInfo implements Serializable {

    private static final long serialVersionUID = -846791671276090816L;

    // Public Claims (Header)
    private String keyId;
    private String issuer;
    private String subject;
    private List<String> audience = new ArrayList<>();
    private Date expiresAt;
    private Date notBefore;
    private Date issuedAt;
    private String jwtId;
    // Private Claims
    private Map<String, Object> privateClaims = new HashMap<>(0);

    private JwtTokenInfo() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtTokenInfo that = (JwtTokenInfo) o;
        return Objects.equals(keyId, that.keyId) &&
                Objects.equals(issuer, that.issuer) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(audience, that.audience) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(notBefore, that.notBefore) &&
                Objects.equals(issuedAt, that.issuedAt) &&
                Objects.equals(jwtId, that.jwtId) &&
                Objects.equals(privateClaims, that.privateClaims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, issuer, subject, audience, expiresAt, notBefore, issuedAt, jwtId, privateClaims);
    }

    public static Builder builder() {
        return new Builder();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static class Builder {
        private String keyId;
        private String issuer;
        private String subject;
        private List<String> audience = new ArrayList<>();
        private Date expiresAt;
        private Date notBefore;
        private Date issuedAt;
        private String jwtId;
        private Map<String, Object> privateClaims = new HashMap<>(0);

        private Builder() {
            super();
        }

        public Builder keyId(String keyId) {
            this.keyId = keyId;
            return this;
        }

        public Builder keyId(Supplier<String> supplier) {
            return keyId(supplier.get());
        }

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder audience(List<String> audience) {
            this.audience = audience;
            return this;
        }

        public Builder audience(String... audience) {
            return audience(Arrays.asList(audience));
        }

        public Builder audience(String audience) {
            List<String> list = new ArrayList<>(1);
            list.add(audience);
            return audience(list);
        }

        public Builder expiresAt(Date expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder expiresAtFuture(long duration, TimeUnit timeUnit) {
            return expiresAt(DateUtils.afterNow(duration, timeUnit));
        }

        public Builder expiresAtFuture(Duration duration) {
            return expiresAtFuture(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        public Builder notBefore(Date notBefore) {
            this.notBefore = notBefore;
            return this;
        }

        public Builder notBeforeFuture(long duration, TimeUnit timeUnit) {
            return notBefore(DateUtils.afterNow(duration, timeUnit));
        }

        public Builder notBeforeFuture(Duration duration) {
            return notBeforeFuture(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        public Builder issuedAt(Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder issuedAtNow() {
            return issuedAt(new Date());
        }

        public Builder jwtId(String jwtId) {
            this.jwtId = jwtId;
            return this;
        }

        public Builder jwtId(Supplier<String> supplier) {
            return jwtId(supplier.get());
        }

        public Builder putPrivateClaim(String key, Boolean value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, Date value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, Double value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, String value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, String[] value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, Integer value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, Integer[] value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, Long value) {
            return putPrivateClaim(key, (Object) value);
        }

        public Builder putPrivateClaim(String key, Long[] value) {
            return putPrivateClaim(key, (Object) value);
        }

        private Builder putPrivateClaim(String key, Object value) {
            this.privateClaims.put(key, value);
            return this;
        }

        public JwtTokenInfo build() {
            JwtTokenInfo info = new JwtTokenInfo();
            info.jwtId = this.jwtId;
            info.keyId = this.keyId;
            info.issuer = this.issuer;
            info.subject = this.subject;
            info.audience = this.audience;
            info.expiresAt = this.expiresAt;
            info.notBefore = this.notBefore;
            info.issuedAt = this.issuedAt;
            info.privateClaims = this.privateClaims;
            return info;
        }
    }

}
