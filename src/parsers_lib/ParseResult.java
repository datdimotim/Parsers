package parsers_lib;

public class ParseResult<T> {
    public final T result;
    public final String errInf;
    public final int posErr;
    private final boolean isError;

    public ParseResult(T result){
        this.result=result;
        this.posErr=-1;
        this.errInf=null;
        this.isError=false;
    }

    public ParseResult(String errInf, int posErr){
        this.result=null;
        this.posErr=posErr;
        this.errInf=errInf;
        this.isError=true;
    }

    public boolean isError(){
        return isError;
    }
}
