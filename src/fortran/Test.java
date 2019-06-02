package fortran;

import parsers_lib.CharStream;
import parsers_lib.ParseResult;
import parsers_lib.Parser;

import java.util.*;
import java.util.function.Predicate;
import static fortran.FortranParser.*;

public class Test{
    public static void main(String[] args) {
        testName();
        testModule();
        testListParams();
        testSubroutine();
        testDeclarationList();
    }

    public static void testDeclarationList(){
        Parser<Map<String,FullType>> declarationList=FortranParser.declarationList();
        String[] accept={
                "integer*1::a",
                "logical*2::a,b",
                "real*8 ::  a  , b",
                "character *  100 :: a, b \t",
                "integer *4 :: a\n integer*8  :: b  \n character*22::c \n",
        };

        String[] wrong={
                "integer*1::",
                "integer*1:: \ninteger *1 a",
                "logical*2::a,a",
                "integer *3 :: a",
                "integer *4::a\nlogical*1::a"
        };

        Map[] paramLists={
                new HashMap<String,FullType>(){{
                    put("a",new FullType(FortranType.INTEGER,1));
                }},
                new HashMap<String,FullType>(){{
                    put("a",new FullType(FortranType.LOGICAL,2));
                    put("b",new FullType(FortranType.LOGICAL,2));
                }},
                new HashMap<String,FullType>(){{
                    put("a",new FullType(FortranType.REAL,8));
                    put("b",new FullType(FortranType.REAL,8));
                }},
                new HashMap<String,FullType>(){{
                    put("a",new FullType(FortranType.CHARACTER,100));
                    put("b",new FullType(FortranType.CHARACTER,100));
                }},
                new HashMap<String,FullType>(){{
                    put("a",new FullType(FortranType.INTEGER,4));
                    put("b",new FullType(FortranType.INTEGER,8));
                    put("c",new FullType(FortranType.CHARACTER,22));
                }},
        };

        for(int i=0;i<accept.length;i++){
            final int ii=i;
            checkPositive(
                    declarationList,accept[i],
                    sub-> sub.equals(paramLists[ii])
            );
        }

        for(int i=0;i<wrong.length;i++){
            checkNegative(declarationList,wrong[i],err->{
                System.out.println(err);
                return true;
            });
        }


    }

    public static void testSubroutine(){
        Parser<Subroutine> subroutine=subroutine();


        String[] accept={
                "subroutine zero\n...\nend\n",
                "subroutine    \t single \t\t (\t first\t  ) \n\n   \n ...\n  \n \n \t end \t subroutine \t single\n",
                "subroutine  double    (first, second) \n...\n   end\t subroutine \n",
                "subroutine   many(first, second, \t third)\n \t ...\n\t enD\n",
        };
        final Subroutine[] subroutines={
                new Subroutine("zero",new ArrayList<>()),
                new Subroutine("single", Arrays.asList("first")),
                new Subroutine("double", Arrays.asList("first","second")),
                new Subroutine("many",Arrays.asList("first","second","third"))
        };

        String[] wrong={"subroutine twin(x,x)\n...\nend\n","subroutine\n...\nend"};
        Predicate[] checkers={
                (Predicate<String>) err-> err.contains("listParams: multiple declaration params"),
                (Predicate<String>) err->err.startsWith("subroutine: "),
                (Predicate<String>) err->true
        };

        for(int i=0;i<accept.length;i++){
            final int ii=i;
            checkPositive(
                    subroutine,accept[i],
                    sub->sub.getName().equals(
                            subroutines[ii].getName())
                            && sub.getParams().equals(subroutines[ii].getParams()
                    )
            );
        }
        for(int i=0;i<wrong.length;i++){
            final int ii=i;
            checkNegative(subroutine,wrong[i],err->checkers[ii].test(err));
        }
    }

    public static void testModule(){
        Parser<String> module=FortranParser.module();
        String[] accept={
                "MoDule a \n...\neNd mOdulE a\n",
                "module    \t aS_1   \n\n  \n ...\n  \n \n \t end \t module \t as_1\n",
                "module  b\n...\n   end\t module\n",
                "module   ccd_\n \t ...\n\t enD\n",
        };
        final String[] names={"a","as_1","b","ccd_"};

        String[] wrong={"module \n a ... end","module as\n..\nend","module ass\n...\nend module as;"};
        String[] mesgs={"decl","body","end"};

        for(int i=0;i<accept.length;i++){
            final int ii=i;
            checkPositive(module,accept[i],name->name.toLowerCase().equals(names[ii]));
        }
        for(int i=0;i<wrong.length;i++){
            final int ii=i;
            checkNegative(module,wrong[i],err->err.startsWith("module: "+mesgs[ii]));
        }
    }

    public static void testListParams(){
        Parser<List<String>> module=FortranParser.listParams();
        String[] accept={
                "(a,b,c,d)",
                "(  as , bAs , daa_w    ,c2  )  ",
                "(a)",
                "(  wW_1 \t)",
                "",
                " s"
        };
        final String[][] names={{"a","b","c","d"},{"as","bAs","daa_w","c2"},{"a"},{"ww_1"},{},{}};

        String[] wrong={"(a,b,d,a)"};

        for(int i=0;i<accept.length;i++){
            final int ii=i;
            checkPositive(module,accept[i],list->{
                for(int iname=0;iname<names[ii].length;iname++){
                    if(!names[ii][iname].toLowerCase().equals(list.get(iname).toLowerCase()))return false;
                }
                return true;
            });
        }
        for(int i=0;i<wrong.length;i++){
            checkNegative(module,wrong[i],err->true);
        }
    }

    public static void testName(){
        Parser<String> name= Lexers.name();
        String[] accept={"func","f","f12","f1f2f32cc3d22","f_12","___asw","ssww2___","s swew 2 2"};
        String[] wrong={" ssa","","2ssq","01qww sdw"};

        for(String a:accept)checkPositive(name,a,s->true);
        for(String w:wrong)checkNegative(name,w,s->true);
    }

    private static <T>void checkPositive(Parser<T> parser, String string, Predicate<T> checker){
        CharStream stream=new CharStream(string);
        ParseResult<T> result=parser.parse(stream);
        if(result.isError()){
            String err=result.describeError(stream);
            System.out.println(err);
            throw new RuntimeException(string);

        }
        if(!checker.test(result.result))throw new RuntimeException(result.result.toString());
    }

    private static <T>void checkNegative(Parser<T> parser,String string,Predicate<String> checkMesg){
        CharStream stream=new CharStream(string);
        ParseResult<T> result=parser.parse(stream);
        if(!result.isError())throw new RuntimeException(string);
        if(!checkMesg.test(result.describeError(stream)))throw new RuntimeException(string+"\n\n"+result.describeError(stream));
    }
}
