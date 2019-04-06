package parsers_lib;

public class CharStream{
    private final String src;
    private int pos=0;

    public CharStream(String src){
        this.src=src;
    }

    public boolean isEnd(){
        return pos>=src.length();
    }

    public char peek(){
        return src.charAt(pos);
    }

    public char get(){
        return src.charAt(pos++);
    }

    public Save save(){
        return new Save(pos);
    }

    public void restore(Save save){
        if(this!=save.getOutsideCharStream())throw new RuntimeException();
        pos=save.pos;
    }

    public int getPos(){
        return pos;
    }

    public class Save{
        public Save(int pos) {
            this.pos = pos;
        }

        private CharStream getOutsideCharStream(){
            return CharStream.this;
        }
        private final int pos;
    }
}
