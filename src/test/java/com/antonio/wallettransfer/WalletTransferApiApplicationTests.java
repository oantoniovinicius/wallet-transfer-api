package com.antonio.wallettransfer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class WalletTransferApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
