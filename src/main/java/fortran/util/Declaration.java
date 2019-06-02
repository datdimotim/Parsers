package fortran.util;

public class Declaration{
    public final Type type;
    public final String name;
    public final String text;

    Declaration(Type type, String name, String text) {
        this.type = type;
        this.name = name;
        this.text = text;
    }

    enum Type{
        MODULE, SUBROUTINE, FUNCTION, EXTERNAL, VARIABLE
    }
}
