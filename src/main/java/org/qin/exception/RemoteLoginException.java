package org.qin.exception;

import org.qin.common.ResultStatus;
import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * @author leijianhui
 * @Description 异地登入异常
 * @date 2019/1/1
 */
public class RemoteLoginException extends RuntimeException {

    private ResultStatus resultStatus;


    public RemoteLoginException() {
    }

    public RemoteLoginException(String message) {
        super(message);
    }


    public RemoteLoginException(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }


    public RemoteLoginException(ResultStatus resultStatus, Throwable cause) {
        super(resultStatus.getMessage(), cause);
    }


    public RemoteLoginException(Throwable cause) {
        super(cause);
    }


    public RemoteLoginException(String message, Object... args) {
        this(message.contains("{}") ? MessageFormatter.arrayFormat(message, args).getMessage() : new MessageFormat(message).format(args));
    }


    public ResultStatus getResultStatus() {
        return resultStatus;
    }
}
