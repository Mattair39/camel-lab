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
                .log("Procesando Archivo: ${file:name}")
                .wireTap("direct:registrarLog")
                .convertBodyTo(String.class)
                .transform().simple("${body.toUpperCase()}")

                .to("file:d:/Cursos/IntegracionSistemas/camel-lab/output")
                .log("Archivo enviado a output: ${file:name} - ${date:now:yyyy-MM-dd HH:mm:ss}")

                .filter(simple("${file:name} != 'inventario.csv'"))
                .to("file:d:/Cursos/IntegracionSistemas/camel-lab/archived")
                .log("Archivo enviado a archived: ${file:name} - ${date:now:yyyy-MM-dd HH:mm:ss}")
                .end();

        from("direct:registrarLog")
                .transform().simple("${date:now:yyyy-MM-dd HH:mm:ss} | Procesando Archivo: ${header.CamelFileName}${sys.line.separator}")
                .to("file:d:/Cursos/IntegracionSistemas/camel-lab/logs"
                        + "?fileName=transfers-${date:now:yyyy-MM-dd-HH'h'mm}.log&fileExist=Append&charset=UTF-8");
    }
}