package parsers_lib;

import java.util.function.Function;

public class ParseResult<T> {
    public final T result;
    final String errInf;
    public final int posErr;
    public final ParseResult<?> causeError;
    private final boolean isError;

    public ParseResult(T result){
        this.result=result;
        this.posErr=-1;
        this.errInf=null;
        this.causeError=null;
        this.isError=false;
    }

    public ParseResult(String errInf, int posErr, ParseResult<?> causeError){
        this.result=null;
        this.posErr=posErr;
        this.errInf=errInf;
        this.causeError=causeError;
        this.isError=true;
    }

    public <R> ParseResult<R> map(Function<T,R> f){
        if(isError())return new ParseResult<>(errInf,posErr,causeError);
        else return new ParseResult<>(f.apply(result));
    }

    public boolean isError(){
        return isError;
    }

    public String subString(String src, int pos, int width){
        return src.substring(Math.max(0,pos-width),Math.min(pos+width,src.length()));
    }

    public String markerString(int pos, int width){
        final int count=Math.min(width,pos);
        StringBuilder r= new StringBuilder();
        for(int i=0;i<count;i++) r.append("-");
        r.append("^");
        return r.toString();
    }

    public String describeError(CharStream stream){
        return describeError(stream.getSrc(),"");
    }

    private String describeError(String src, String tabs){
        String err=tabs+errInf+"\n";
        err+=tabs+subString(src,posErr,10)+"\n";
        err+=tabs+markerString(posErr,10)+"\n";
        if(causeError!=null)return err+causeError.describeError(src,tabs+"\t");
        else return err;
    }
}
