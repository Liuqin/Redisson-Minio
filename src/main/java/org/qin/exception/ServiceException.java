package org.qin.exception;

import org.qin.common.ResultStatus;
import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * @author wjm
 * @Date 2017/12/5
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {
    }


    private ResultStatus resultStatus;


    public ServiceException(String message) {
        super(message);
    }


    public ServiceException(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }


    public ServiceException(ResultStatus resultStatus, Throwable cause) {
        super(resultStatus.getMessage(), cause);
    }


    public ServiceException(Throwable cause) {
        super(cause);
    }


    public ServiceException(String message, Object... args) {
        this(message.contains("{}") ? MessageFormatter.arrayFormat(message, args).getMessage() : new MessageFormat(message).format(args));
    }


    public ResultStatus getResultStatus() {
        return resultStatus;
    }
}
