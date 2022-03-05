package message_router.core;

public class Acknowledgement {
    private Boolean success;

    private Acknowledgement(Boolean success){
        this.success = success;
    }

    public static Acknowledgement successful(){
        return new Acknowledgement(true);
    }

    public static Acknowledgement failure(){
        return new Acknowledgement(false);
    }

    public Boolean isSuccessful(){
        return success;
    }

    public Boolean isFailure(){
        return !success;
    }
}
