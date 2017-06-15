package nl.tudelft.b_b_w.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tudelft.b_b_w.BuildConfig;
import nl.tudelft.b_b_w.blockchain.User;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class APITest {

    private User owner;
    private API api;

    /**
     * Initialize BlockController before every test
     * And initialize a dummy block _block
     */
    @Before
    public final void setUp() throws Exception {
        owner = new User("Jeff", "iban", ED25519.getPublicKey(ED25519.generatePrivateKey()));
        api = new API(owner, RuntimeEnvironment.application);
    }

    @Test
    public final void addContactToChainTest() throws Exception {
//        api.addContactToChain(new User("Nick", "iban2", ED25519.getPublicKey(ED25519.generatePrivateKey())));
//        List<Block> list = new ArrayList<>();
//        System.out.print(api.getBlocks(owner));
//        assertEquals(api.getBlocks(owner), list);
    }


}
