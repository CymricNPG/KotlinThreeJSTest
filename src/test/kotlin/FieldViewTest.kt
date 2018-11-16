import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestFieldView {

    @Test
    fun testInit() {
        val baseField = EndlessField(5)
        val fieldView = FieldView(5, 5, baseField)
        assertNotNull(fieldView)
        assertEquals(5, fieldView.xsize)
    }
}