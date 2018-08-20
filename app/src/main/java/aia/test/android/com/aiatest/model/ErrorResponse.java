
package aia.test.android.com.aiatest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import aia.test.android.com.aiatest.constants.StringConstants;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        StringConstants.RESPONSE_STATUS,
        StringConstants.RESPONSE_MSG
})
public class ErrorResponse {

    @JsonProperty(StringConstants.RESPONSE_STATUS)
    private String responseCode;
    @JsonProperty(StringConstants.RESPONSE_MSG)
    private String responseMsg;


    /**
     * @return The responseCode
     */
    @JsonProperty(StringConstants.RESPONSE_STATUS)
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode The responseCode
     */
    @JsonProperty(StringConstants.RESPONSE_STATUS)
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return The responseMsg
     */
    @JsonProperty(StringConstants.RESPONSE_MSG)
    public String getResponseMsg() {
        return responseMsg;
    }

    /**
     * @param responseMsg The responseMsg
     */
    @JsonProperty(StringConstants.RESPONSE_MSG)
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }


}
