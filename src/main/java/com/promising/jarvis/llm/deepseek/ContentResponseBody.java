package com.promising.jarvis.llm.deepseek;


import com.google.gson.annotations.SerializedName;

public class ContentResponseBody {
    @SerializedName("success")
    private boolean success;
    @SerializedName("command")
    private String command;
    @SerializedName("additional_info")
    private String additionalInfo;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    @Override
    public String toString() {
        return "ContentResponseBody{" +
                "success=" + success +
                ", command='" + command + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}