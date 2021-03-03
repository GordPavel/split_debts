package my.org.splitwise

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest(classes = SplitwiseApplication)
@RunWith(SpringRunner)
@ActiveProfiles('test')
class ContextStartsTest {

    @Autowired
    SplitwiseApplication application

    def 'Context starts'() {
        expect:
        application != null
    }
}
