package ro.contezi.shopping.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ro.contezi.shopping.ConfigurableUser;
import ro.contezi.shopping.GraphApi;
import ro.contezi.shopping.list.action.ShoppingListAction;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingIntegration.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("itest")
public class SeleniumFixture {
    private static final int SECONDS_TO_WAIT = 300;
    private static final AtomicBoolean INITIALIZED_WEBHOOK = new AtomicBoolean(false);
    @Autowired
    private WebDriver webDriver;
    @Value("${facebook.messenger.url}")
    private String baseUrl;
    @Autowired
    private ConfigurableUser user;
    @Autowired
    private GraphApi graphApi;
    @Autowired
    private Ngrok ngrok;
    @Value("${application.live.url}")
    private String alwaysUpUrl;

    @Before
    public void setUp() throws InterruptedException {
        if (!INITIALIZED_WEBHOOK.getAndSet(true)) {
            graphApi.registerWebhook(ngrok.getUrl() + "/webhook");
        }
        webDriver.get(baseUrl);
        login(user);
    }

    @After
    public void tearDown() {
        webDriver.quit();
    }

    protected WebElement findElement(By by, int secondsToWait) {
        return new WebDriverWait(webDriver, secondsToWait)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected WebElement findElement(By by) {
        return findElement(by, SECONDS_TO_WAIT);
    }

    protected void typeMessageAndAwaitResponse(String message) throws InterruptedException {
        WebElement messageArea = findElement(By.cssSelector("[aria-label='New message']"));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(messageArea);
        actions.click();
        actions.sendKeys(message, Keys.RETURN);
        actions.build().perform();
        long millisToWait = TimeUnit.MILLISECONDS.convert(SECONDS_TO_WAIT, TimeUnit.SECONDS);
        long millisStep = 100;
        do {
            Thread.sleep(millisStep);
            millisToWait -= millisStep;
            if (millisToWait < 0) {
                throw new IllegalStateException("Timeout waiting for shopping list reply");
            }
        }
        while (!penultimateMessage().equals(ShoppingListAction.removeUnicode(message)));
    }

    protected List<String> getAllMessages() {
        return findElement(By.cssSelector("[aria-label='Messages']"))
                .findElements(By.cssSelector("div[body]"))
                .stream().map(e -> e.getAttribute("body"))
                .collect(Collectors.toList());
    }

    protected void login(ConfigurableUser user) {
        findElement(By.id("email")).sendKeys(user.getEmail());
        findElement(By.id("pass")).sendKeys(user.getPassword());
        findElement(By.id("loginbutton")).click();
    }

    protected String penultimateMessage() {
        List<String> allMessages = getAllMessages();
        if (allMessages.size() > 1) {
            return allMessages.get(allMessages.size() - 2);
        }
        return "";
    }

    protected String lastMessage() {
        List<String> allMessages = getAllMessages();
        if (allMessages.isEmpty()) {
            return "";
        }
        return allMessages.get(allMessages.size() - 1);
    }
}
