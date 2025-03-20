package edu.ustb.common.exception;

import edu.ustb.common.enums.ResponseCode;

/**
 * @author: ljr-YingWu
 * @date: 2024/1/4 10:35
 * @description:
 */
public class LimitedException  extends BaseException {

 private static final long serialVersionUID = -5534700534739261461L;

 public LimitedException(ResponseCode code) {
  super(code.getMessage(), code);
 }

 public LimitedException(Throwable cause, ResponseCode code) {
  super(code.getMessage(), cause, code);
 }


}
