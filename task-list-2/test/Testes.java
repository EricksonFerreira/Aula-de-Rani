// Importa as bibliotecas necessárias
// import static org.junit.Assert.*;
// import static org.hamcrest.CoreMatchers.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;


import org.junit.Rule;
import org.junit.rules.TestName;


// Classe com os métodos de teste
public class Testes extends WebTest {

    // @Rule
    // public TestName name = new TestName();

    @Before
    public void init() {
        // System.out.println(name.getMethodName());
        get("index.php");
    }

    private static void clear() {
        get("_base/clear.php");
    }

    private static void wait(By by) {
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private static void assertInput(String name) {
        List<WebElement> inputs = driver.findElements(By.name(name));
        assertThat(inputs).as("Falta o input[name=\"" + name + "\"]").hasSize(1);
    }

    private static void fillInput(String name, String value) {
        assertInput(name);
        WebElement input = driver.findElement(By.name(name));
        input.sendKeys(value);

    }

    private static void fillLoginForm(String user, String password) {
        get("login.php");
        wait(By.tagName("form"));

        fillInput("user", user);
        fillInput("password", password);
    }

    private static void fillRegisterForm(String user, String password, String password2) {
        get("register.php");
        wait(By.tagName("form"));

        fillInput("user", user);
        fillInput("password", password);
        fillInput("password2", password2);
    }

    private static void fillTaskForm(String title) {
        get("index.php");
        wait(By.tagName("form"));

        fillInput("title", title);
    }

    private static void submitForm() {
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        wait(By.tagName("body"));
    }

    private static int getSessionSize() {
        String lastUrl = driver.getCurrentUrl();
        get("_base/checkSession.php");
        int size = Integer.parseInt(
            driver.findElement(By.tagName("body")).getText()
        );
        get(lastUrl);
        return size;
    }


    // ----------------------------------------------------------------
    // ----------------------------------------------------------------


    // @Test
    // public void index_php_nao_deveria_ser_acessivel_sem_estar_logado() {
    //     clear();

    //     get("index.php");
    //     wait(By.tagName("body"));

    //     assertThat(driver.getCurrentUrl()).doesNotContain("index.php");
    // }

    @Test
    public void acessar_index_php_sem_estar_logado_deveria_redirecionar_para_login_php() {
        clear();

        get("index.php");
        wait(By.tagName("body"));

        assertThat(driver.getCurrentUrl()).as("A index só deveria ser acessível para usuários logados - acessos sem permissão deveriam levar para login.php").endsWith("login.php");
    }

    @Test
    public void deveria_existir_um_form_de_login_em_login_php() {
        clear();

        get("login.php");
        wait(By.tagName("body"));

        assertThat(driver.findElements(By.tagName("form"))).as("Deveria ter apenas 1 form").hasSize(1);

        WebElement form = driver.findElement(By.tagName("form"));

        assertInput("user");
        assertInput("password");
    }

    @Test
    public void deveria_existir_um_form_de_registro_em_register_php() {
        clear();

        get("register.php");
        wait(By.tagName("body"));

        assertThat(driver.findElements(By.tagName("form"))).as("Deveria ter apenas 1 form").hasSize(1);

        WebElement form = driver.findElement(By.tagName("form"));

        assertInput("user");
        assertInput("password");
        assertInput("password2");
    }

    @Test
    public void nao_deveria_logar_sem_usuario_cadastrado() {
        clear();

        fillLoginForm("hulk", "natasha");
        submitForm();

        get("index.php");
        assertThat(driver.getCurrentUrl()).as("Sua verificação para login está certa? Loguei com um usuário não cadastrado").doesNotContain("index.php");
        assertThat(getSessionSize()).as("A sessão deveria estar vazia").isEqualTo(0);
    }

    @Test
    public void deveria_registrar_um_novo_usuario_e_fazer_login() {
        clear();

        fillRegisterForm("hulk", "natasha", "natasha");
        submitForm();

        fillLoginForm("hulk", "natasha");
        submitForm();

        wait(By.tagName("body"));

        assertThat(getSessionSize()).as("Não deveria ter alguma coisa na sessão?").isGreaterThan(0);
    }

    @Test
    public void apos_fazer_login_deveria_redirecionar_para_index_php() {
        clear();

        fillRegisterForm("hulk", "natasha", "natasha");
        submitForm();

        fillLoginForm("hulk", "natasha");
        submitForm();

        wait(By.tagName("body"));

        assertThat(driver.getCurrentUrl()).as("Está sendo possívelo logar com um novo usuário?").endsWith("index.php");
        assertThat(getSessionSize()).as("Não deveria ter alguma coisa na sessão?").isGreaterThan(0);
    }

    @Test
    public void nao_deveria_registrar_um_usuario_se_as_senhas_forem_diferentes() {
        clear();

        fillRegisterForm("hulk", "natasha", "natashaa");
        submitForm();


        fillLoginForm("hulk", "natasha");
        submitForm();

        assertThat(driver.getCurrentUrl()).as("Você está checando se as senhas (password e password2) são iguais?").doesNotContain("index.php");


        fillLoginForm("hulk", "natashaa");
        submitForm();

        assertThat(driver.getCurrentUrl()).as("Você está checando se as senhas (password e password2) são iguais?").doesNotContain("index.php");
    }

    @Test
    public void nao_deveria_registrar_dois_usuarios_com_o_mesmo_nome() {
        clear();

        fillRegisterForm("tony", "pepper", "pepper");
        submitForm();

        fillRegisterForm("tony", "hulk", "hulk");
        submitForm();


        fillLoginForm("tony", "hulk");
        submitForm();

        assertThat(driver.getCurrentUrl()).as("Não deveria ser possível haver dois usuários com o mesmo nome").doesNotContain("index.php");
        assertThat(getSessionSize()).as("Não deveria ter nada na sessão se não está logado").isEqualTo(0);


        fillLoginForm("tony", "pepper");
        submitForm();

        assertThat(driver.getCurrentUrl()).endsWith("index.php");

    }

    @Test
    public void nao_deveria_logar_com_um_usuario_inexistente() {
        clear();

        fillRegisterForm("tony", "pepper", "pepper");
        submitForm();

        fillLoginForm("vader", "luke");
        submitForm();

        assertThat(driver.getCurrentUrl()).doesNotContain("index.php");
    }

    @Test
    public void nao_deveria_logar_com_uma_senha_errada() {
        clear();

        fillRegisterForm("tony", "pepper", "pepper");
        submitForm();

        fillLoginForm("tony", "peppper");
        submitForm();

        assertThat(driver.getCurrentUrl()).doesNotContain("index.php");
    }

    public void deveria_logar_e_redirecionar_o_usuario_para_a_index() {
        clear();

        fillRegisterForm("tony", "thor", "thor");
        submitForm();

        fillLoginForm("tony", "thor");
        submitForm();

        assertThat(driver.getCurrentUrl()).as("Você está redirecionando para a index depois que loga?").endsWith("index.php");
    }

    @Test
    public void apos_logar_deveria_ter_um_link_para_deslogar() {
        clear();

        fillRegisterForm("tony", "thor", "thor");
        submitForm();

        fillLoginForm("tony", "thor");
        submitForm();

        assertThat(driver.findElements(By.className("logout"))).as("Deveria ter um link com a classe \"logout\"").hasSize(1);
    }

    @Test
    public void o_botao_de_logout_deveria_limpar_a_sessao() {
        clear();

        fillRegisterForm("tony", "thor", "thor");
        submitForm();

        fillLoginForm("tony", "thor");
        submitForm();

        assertThat(driver.findElements(By.className("logout"))).as("Deveria ter  um link para logout").hasSize(1);
        WebElement logout = driver.findElement(By.className("logout"));
        logout.click();

        get("index.php");
        assertThat(driver.getCurrentUrl()).as("Não deveria ser possível acessar index.php logo após fazer logout").doesNotContain("index.php");
    }

    @Test
    public void deveria_existir_um_formulario_com_a_classe_addtask_em_index_php_para_adicionar_tarefa() {
        clear();

        fillRegisterForm("ash", "pikachu", "pikachu");
        submitForm();

        fillLoginForm("ash", "pikachu");
        submitForm();

        assertThat(driver.findElements(By.className("addtask"))).as("Você adicionou a classe 'addtask' ao formulário?").hasSize(1);
    }

    @Test
    public void deveria_existir_uma_tabela_com_a_classe_tasks_em_index() {
        clear();

        fillRegisterForm("ash", "pikachu", "pikachu");
        submitForm();

        fillLoginForm("ash", "pikachu");
        submitForm();

        assertThat(driver.findElements(By.className("tasks"))).as("Você adicionou a classe 'tasks' à tabela?").hasSize(1);
    }

    @Test
    public void deveria_adicionar_uma_nova_tarefa() {
        clear();

        fillRegisterForm("ash", "pikachu", "pikachu");
        submitForm();

        fillLoginForm("ash", "pikachu");
        submitForm();

        fillTaskForm("Domar o Charizard");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("task"))).as("Tem certeza que adicionou a classe 'task' em cada linha da tabela?").hasSize(1);
        assertThat(driver.findElement(By.className("tasks")).findElement(By.className("task")).getText()).as("O conteúdo da tabela está certo?").contains("Domar o Charizard");
    }

    @Test
    public void as_tarefas_de_um_usuario_nao_deveriam_ser_visiveis_para_outro_usuario() {
        clear();

        fillRegisterForm("ash", "pikachu", "pikachu");
        submitForm();

        fillRegisterForm("anakin", "vader", "vader");
        submitForm();

        fillLoginForm("ash", "pikachu");
        submitForm();

        fillTaskForm("Domar o Charizard");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("task"))).as("Tem certeza que adicionou a classe 'task' em cada linha da tabela?").hasSize(1);
        driver.findElement(By.className("logout")).click();

        fillLoginForm("anakin", "vader");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("task"))).as("As tarefas estão sendo filtradas por usuário?").hasSize(0);

    }

    @Test
    public void deveria_haver_uma_acao_para_remover_uma_tarefa() {
        clear();

        fillRegisterForm("ash", "misty", "misty");
        submitForm();

        fillLoginForm("ash", "misty");
        submitForm();

        fillTaskForm("catch them all");
        submitForm();

        fillTaskForm("win battles");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("rmtask"))).as("Tem certeza que adicionou a classe 'rmtask' no link de remover?").hasSize(2);
    }

    @Test
    public void deveria_remover_uma_tarefa_ao_clicar_no_link() {
        clear();

        fillRegisterForm("ash", "misty", "misty");
        submitForm();

        fillLoginForm("ash", "misty");
        submitForm();

        fillTaskForm("catch them all");
        submitForm();

        fillTaskForm("win battles");
        submitForm();



        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("rmtask"))).as("Tem certeza que adicionou a classe 'rmtask' no link de remover?").hasSize(2);
        driver.findElement(By.className("tasks")).findElement(By.className("rmtask")).click();
        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("task"))).as("Está mesmo removendo?").hasSize(1);
    }

    @Test
    public void remover_uma_tarefa_nao_deveria_remover_tarefas_de_outros_usuarios() {
        clear();

        fillRegisterForm("ash", "misty", "misty");
        submitForm();

        fillLoginForm("ash", "misty");
        submitForm();

        fillTaskForm("catch them all");
        submitForm();

        fillTaskForm("win battles");
        submitForm();

        driver.findElement(By.className("logout")).click();


        fillRegisterForm("anakin", "vader", "vader");
        submitForm();

        fillLoginForm("anakin", "vader");
        submitForm();

        fillTaskForm("Controlar a força");
        submitForm();

        fillTaskForm("Destruir planetas");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("rmtask"))).as("Tem certeza que adicionou a classe 'rmtask' no link de remover?").hasSize(2);
        driver.findElement(By.className("tasks")).findElement(By.className("rmtask")).click();
        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("task"))).as("Está mesmo removendo?").hasSize(1);


        driver.findElement(By.className("logout")).click();

        fillLoginForm("ash", "misty");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("task"))).as("Está removendo só de um usuário?").hasSize(2);

    }

    // para a próxima aula =)
    @Test
    public void deveria_adicionar_uma_nova_tarefa_com_a_classe_todo() {
        clear();

        fillRegisterForm("ash", "misty", "misty");
        submitForm();

        fillLoginForm("ash", "misty");
        submitForm();

        fillTaskForm("win the first gym");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("todo"))).as("Tem certeza que adicionou a classe 'todo' à tarefa?").hasSize(1);
        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("done"))).as("Tem certeza que a classe 'done' está adicionada corretamente?").hasSize(0);
    }

    @Test
    public void deveria_haver_uma_acao_para_marcar_uma_tarefa_todo_como_done() {
        clear();

        fillRegisterForm("ash", "misty", "misty");
        submitForm();

        fillLoginForm("ash", "misty");
        submitForm();

        fillTaskForm("win the first gym");
        submitForm();

        fillTaskForm("win the second gym");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("taskdone"))).as("Tem certeza que adicionou a classe 'taskdone' no link de marcar como feita?").hasSize(2);
    }

    @Test
    public void deveria_trocar_a_classe_da_tarefa_de_todo_para_done_ao_clicar_no_link() {
        clear();

        fillRegisterForm("ash", "misty", "misty");
        submitForm();

        fillLoginForm("ash", "misty");
        submitForm();

        fillTaskForm("win the first gym");
        submitForm();

        fillTaskForm("win the second gym");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("taskdone"))).as("Tem certeza que adicionou a classe 'taskdone' no link de marcar como feita?").hasSize(2);
        driver.findElement(By.cssSelector(".tasks .taskdone")).click();
        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("done"))).as("Tem certeza que adicionou a classe 'done' às tarefas feitas?").hasSize(1);
    }

    @Test
    public void deveria_haver_uma_acao_para_remover_uma_tarefa_marcada_como_done() {
        clear();

        fillRegisterForm("ash", "misty", "misty");
        submitForm();

        fillLoginForm("ash", "misty");
        submitForm();

        fillTaskForm("win the first gym");
        submitForm();

        fillTaskForm("win the second gym");
        submitForm();

        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("taskdone"))).as("Tem certeza que adicionou a classe 'taskdone' no link de marcar como feita?").hasSize(2);
        driver.findElement(By.cssSelector(".tasks .taskdone")).click();
        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("done"))).as("Tem certeza que adicionou a classe 'done' às tarefas feitas?").hasSize(1);
        assertThat(driver.findElement(By.className("tasks")).findElements(By.className("rmtask"))).as("Tem certeza que adicionou a classe 'rm' no link de marcar como feita?").hasSize(2);
    }

}
