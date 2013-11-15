package org.aldeon.dbstub;

import org.aldeon.db.exception.IdentifierAlreadyPresentException;
import org.aldeon.db.exception.UnknownIdentifierException;
import org.aldeon.db.exception.UnknownParentException;
import org.aldeon.model.Identifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class XorManagerImplTest {

    @Test
    public void shouldStoreTopic() throws UnknownIdentifierException, UnknownParentException, IdentifierAlreadyPresentException {

        XorManagerImpl mgr = new XorManagerImpl();

        Identifier id = mock(Identifier.class);
        Identifier topic = mock(Identifier.class);

        when(id.isEmpty()).thenReturn(false);
        when(topic.isEmpty()).thenReturn(true);

        mgr.putId(id, topic);

        assertTrue(mgr.contains(id));
    }

    @Test(expected = UnknownParentException.class)
    public void shouldThrowExceptionWhenInsertingResponseToUnknownParent() throws UnknownParentException, UnknownIdentifierException, IdentifierAlreadyPresentException {

        XorManagerImpl mgr = new XorManagerImpl();

        Identifier id = mock(Identifier.class);
        Identifier parent = mock(Identifier.class);

        when(id.isEmpty()).thenReturn(false);
        when(parent.isEmpty()).thenReturn(false);

        mgr.putId(id, parent);
    }

    @Test(expected = UnknownIdentifierException.class)
    public void shouldThrowExceptionWhenFetchingUnknownIdentifier() throws UnknownIdentifierException {

        XorManagerImpl mgr = new XorManagerImpl();
        Identifier id = mock(Identifier.class);

        mgr.getXor(id);
    }

    @Test(expected = IdentifierAlreadyPresentException.class)
    public void shouldNotAllowZeroIdentifier() throws UnknownParentException, IdentifierAlreadyPresentException {

        XorManagerImpl mgr = new XorManagerImpl();

        Identifier id = mock(Identifier.class);
        Identifier parent = mock(Identifier.class);

        when(id.isEmpty()).thenReturn(true);
        when(parent.isEmpty()).thenReturn(true);

        mgr.putId(id, parent);
    }

    @Test
    public void shouldStoreIdentifierWhenParentIsPresent() throws UnknownParentException, UnknownIdentifierException, IdentifierAlreadyPresentException {

        XorManagerImpl mgr = new XorManagerImpl();

        Identifier id = mock(Identifier.class);
        Identifier parent = mock(Identifier.class);
        Identifier topic = mock(Identifier.class);

        when(id.isEmpty()).thenReturn(false);
        when(parent.isEmpty()).thenReturn(false);
        when(topic.isEmpty()).thenReturn(true);

        mgr.putId(parent, topic);
        mgr.putId(id, parent);

        assertTrue(mgr.contains(id));
    }

    @Test
    public void shouldUpdateParentXorWhenChildIsInserted() throws UnknownParentException, IdentifierAlreadyPresentException, UnknownIdentifierException {

        XorManagerImpl mgr = new XorManagerImpl();

        Identifier id = mock(Identifier.class);
        Identifier parent = mock(Identifier.class);
        Identifier topic = mock(Identifier.class);

        Identifier xor = mock(Identifier.class);

        when(id.isEmpty()).thenReturn(false);
        when(parent.isEmpty()).thenReturn(false);
        when(topic.isEmpty()).thenReturn(true);

        when(parent.xor(id)).thenReturn(xor);
        when(id.xor(parent)).thenReturn(xor);

        mgr.putId(parent, topic);
        mgr.putId(id, parent);

        assertEquals(mgr.getXor(parent), xor);
    }

    @Test
    public void shouldRemoveChildWhenParentIsRemoved() throws UnknownParentException, IdentifierAlreadyPresentException, UnknownIdentifierException {

        XorManagerImpl mgr = new XorManagerImpl();

        Identifier id = mock(Identifier.class);
        Identifier parent = mock(Identifier.class);
        Identifier topic = mock(Identifier.class);

        when(id.isEmpty()).thenReturn(false);
        when(parent.isEmpty()).thenReturn(false);
        when(topic.isEmpty()).thenReturn(true);

        mgr.putId(parent, topic);
        mgr.putId(id, parent);

        mgr.delId(parent);

        assertFalse(mgr.contains(id));
    }
}
