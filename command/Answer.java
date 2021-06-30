package server.command;

import java.io.Serializable;

public class Answer implements Serializable {

    private static final long serialVersionUID = 9633796805834581L;
    private String answer;

    public void setKey(String answer){
        this.answer = answer;
    }

    public String getKey(){
        return answer;
    }

}
