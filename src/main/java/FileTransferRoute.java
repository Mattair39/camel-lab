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
            .filter(header("CamelFileName").endsWith(".csv"))
            .log("Procesando archivo: ${file:name}")
            .wireTap("direct:registrarLog")
            .convertBodyTo(String.class)
            .transform().simple("${body.toUpperCase()}")
            .to("file:d:/Cursos/IntegracionSistemas/camel-lab/output")
            .to("file:d:/Cursos/IntegracionSistemas/camel-lab/archived");

        from("direct:registrarLog")
            .transform().simple("${date:now:yyyy-MM-dd HH:mm:ss} | Procesado: ${header.CamelFileName}\n")
            .to("file:d:/Cursos/IntegracionSistemas/camel-lab/logs"
                + "?fileName=transfers-${date:now:yyyy-MM-dd-HH'h'mm}.log&fileExist=Append&charset=UTF-8");
    }
}