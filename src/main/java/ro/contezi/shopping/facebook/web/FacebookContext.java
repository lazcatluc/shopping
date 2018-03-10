package ro.contezi.shopping.facebook.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookContext {

    public static final FacebookContext ANONYMOUS = new FacebookContext("", "",
            ThreadType.USER_TO_PAGE, 0, "");

    private final String threadId;
    private final String userId;
    private final long issuedAt;
    private final String pageId;
    private final ThreadType threadType;

    @JsonCreator
    public FacebookContext(@JsonProperty("tid") String threadId,
                           @JsonProperty("psid") String userId,
                           @JsonProperty("thread_type") ThreadType threadType,
                           @JsonProperty("issued_at") long issuedAt,
                           @JsonProperty("page_id") String pageId) {
        this.threadId = threadId;
        this.userId = userId;
        this.threadType = threadType;
        this.issuedAt = issuedAt;
        this.pageId = pageId;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getUserId() {
        return userId;
    }

    public ThreadType getThreadType() {
        return threadType;
    }

    @JsonIgnore
    public LocalDateTime getIssuedDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(issuedAt),
                TimeZone.getDefault().toZoneId());
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public String getPageId() {
        return pageId;
    }

    public void requireUserEquals(String userId) {
        if (!Objects.equals(this.userId, userId)) {
            throw new ValidationException("Different user id found");
        }
    }

    public void requireLogged() {
        if (ANONYMOUS.equals(this)) {
            throw new ValidationException("Annonymous user found");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FacebookContext that = (FacebookContext) o;

        return new EqualsBuilder()
                .append(issuedAt, that.issuedAt)
                .append(threadId, that.threadId)
                .append(userId, that.userId)
                .append(pageId, that.pageId)
                .append(threadType, that.threadType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(threadId)
                .append(userId)
                .append(issuedAt)
                .append(pageId)
                .append(threadType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("threadId", threadId)
                .append("userId", userId)
                .append("issuedAt", getIssuedDateTime())
                .append("pageId", pageId)
                .append("threadType", threadType)
                .toString();
    }
}
