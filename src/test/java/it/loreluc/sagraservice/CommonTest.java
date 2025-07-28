package it.loreluc.sagraservice;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles( value = {"test"})
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@DBRider
@DBUnit(schema = "sagra_test", caseSensitiveTableNames = true)
@Import(TestContainerConfig.class)
public abstract class CommonTest {

	@Autowired
	protected WebApplicationContext context;

	@Autowired
	protected MockMvc mockMvc;
}
