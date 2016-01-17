package me.jamespan.tech.smartcrop.domain;

/**
 *
 * @author panjiabang
 * @since  12/31/15
 */
public class Result {
    boolean bodyFound;
    Box body;

    private Result(boolean bodyFound, Box body) {
        this.bodyFound = bodyFound;
        this.body = body;
    }

    public static Result notFound() {
        return new Result(false, null);
    }

    public static Result with(Box body) {
        return new Result(true, body);
    }

    public boolean isBodyFound() {
        return bodyFound;
    }

    public Box getBody() {
        return body;
    }
}
