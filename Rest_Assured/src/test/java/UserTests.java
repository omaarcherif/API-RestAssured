import Models.ResponseModel_DESerialization.CreateUserSuccessfullyModel;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import Models.ResponseModel_DESerialization.GetSingleUserModel;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utilities.Constants;
import utilities.Headers;
import utilities.LoginUtils;
import utilities.UserUtils;

import java.util.HashMap;
import java.util.Map;

public class UserTests {
    SoftAssert softAssert = new SoftAssert();
    @BeforeClass
    public void Login() {
        JsonPath jsonPath = LoginUtils.login(Constants.email, Constants.password, 200).jsonPath();
        String Token = jsonPath.getString("token");
        System.out.println("Token is = " + Token);
    }

    @Test
    public void getSingleUser() {
        Integer ID = 2;
        GetSingleUserModel responseModel = UserUtils.getSingleUser(ID, 200);

        softAssert.assertEquals(responseModel.data.id, ID, "ID is not the same");
        softAssert.assertNotNull(responseModel.data.email, "email is null");
        softAssert.assertNotNull(responseModel.data.firstName, "first name is null");
        softAssert.assertAll();
    }

    @Test
    public void CreateUserSuccessful() {
        String name = "morpheus";
        String job = "leader";
        CreateUserSuccessfullyModel responseModel = UserUtils.CreateUserSuccessfully(name, job, 201);

        softAssert.assertEquals(responseModel.name, name, "name is not the same");
        softAssert.assertEquals(responseModel.job, job, "job is not the same");
        softAssert.assertNotNull(responseModel.id, "id is null");
        softAssert.assertNotNull(responseModel.createdAt, "createdAt is null");
    }

    @Test
    public void getListOfUsers() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");

        RestAssured.given().headers(Headers.generalHeaders())
                .queryParams(queryParams)
                .get(Constants.baseUrl + Constants.UserEndPoint)
                .then().statusCode(200);
    }

    @Test
    public void createUserNameEmpty() {
        Response response = UserUtils.CreateUser("", "Tester", 201);
    }

    @Test
    public void CreateUserJobEmpty() {
        Response response = UserUtils.CreateUser("Omar", "", 201);
        JsonPath jsonPath = response.jsonPath();
    }
}
