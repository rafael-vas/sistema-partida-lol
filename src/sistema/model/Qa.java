package sistema.model;
 
public class Qa extends Usuario {
 
    public Qa(int id, String nome) {
        super(id, nome);
    }
 
    @Override
    public String getTipo() {
        return "QA";
    }
}
 