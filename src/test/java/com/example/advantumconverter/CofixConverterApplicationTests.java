package com.example.advantumconverter;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.stream.Collectors;

import static org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Operation.EQUAL;

@SpringBootTest
class advantumconverterApplicationTests {

    @Test
    void contextLoads() {
        String text1 = "ABCDELMNXXXXXXXXXX";
        String text2 = "ABCFGLMN";
        DiffMatchPatch dmp = new DiffMatchPatch();
        LinkedList<DiffMatchPatch.Diff> diff = dmp.diffMain(text1, text2, false);

        System.out.println(diff.stream()
				.filter(dif -> dif.operation != EQUAL)
				.collect(Collectors.toList()));

		System.out.println(dmp.diffPrettyHtml(diff.stream()
				.filter(dif -> dif.operation != EQUAL)
				.collect(Collectors.toList())));

		System.out.println(dmp.diffText1(diff.stream()
				.filter(dif -> dif.operation != EQUAL)
				.collect(Collectors.toList())));

		System.out.println(dmp.diffText2(diff.stream()
				.filter(dif -> dif.operation != EQUAL)
				.collect(Collectors.toList())));
        int i = 0;
    }

}
