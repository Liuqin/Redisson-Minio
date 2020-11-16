package org.qin.exception;

import org.qin.common.ResultStatus;
import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;

/**
 * @author leijianhui
 * @Description 版本升级
 * @date 2019/2/28
 */
public class VersionNoException extends RuntimeException {

    public VersionNoException() {
    }


    private ResultStatus resultStatus;


    public VersionNoException(String message) {
        super(message);
    }


    public VersionNoException(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }


    public VersionNoException(ResultStatus resultStatus, Throwable cause) {
        super(resultStatus.getMessage(), cause);
    }


    public VersionNoException(Throwable cause) {
        super(cause);
    }


    public VersionNoException(String message, Object... args) {
        this(message.contains("{}") ? MessageFormatter.arrayFormat(message, args).getMessage() : new MessageFormat(message).format(args));
    }


    public ResultStatus getResultStatus() {
        return resultStatus;
    }
}
