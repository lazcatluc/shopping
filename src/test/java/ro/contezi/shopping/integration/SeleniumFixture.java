package ro.contezi.shopping.integration;

import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ro.contezi.shopping.ConfigurableUser;

public interface SeleniumFixture {
    void test();

    WebDriver getWebDriver();

    default WebElement findElement(By by, int secondsToWait) {
        return new WebDriverWait(getWebDriver(), secondsToWait)
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    default WebElement findElement(By by) {
        return findElement(by, 30);
    }

    default void sendMessage(String message) {
        WebElement messageArea = findElement(By.cssSelector("[aria-label='New message']"));
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(messageArea);
        actions.click();
        actions.sendKeys(message, Keys.RETURN);
        actions.build().perform();
    }

    default List<String> getAllMessages() {
        return Arrays.asList(findElement(By.cssSelector("[aria-label='Messages']"))
                .getText().split("\n"));
    }

    default void login(ConfigurableUser user) {
        findElement(By.id("email")).sendKeys(user.getEmail());
        findElement(By.id("pass")).sendKeys(user.getPassword());
        findElement(By.id("loginbutton")).click();
    }
}
