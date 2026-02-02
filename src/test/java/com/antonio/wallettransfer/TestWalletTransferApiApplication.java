package com.antonio.wallettransfer;

import org.springframework.boot.SpringApplication;

public class TestWalletTransferApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(WalletTransferApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
