import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class FileTransferRoute extends RouteBuilder {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new FileTransferRoute());
        main.run();
    }

    @Override
    public void configure() throws Exception {
        from("file:d:/Cursos/IntegracionSistemas/camel-lab/input?noop=true")
            .log("Procesando archivo: ${file:name}")
            .transform().simple("${body.toUpperCase()}")
            .to("file:d:/Cursos/IntegracionSistemas/camel-lab/output");
    }
}