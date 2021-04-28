package JUnit

import Comms
import DataClass
import JArray
import JObject
import JSONType
import JValue
import Serializer
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class JUnitTests {

    fun testObject(): JSONType{
        val object1 = JObject()

        val value1 = JValue("a")
        val value2 = JValue(11)

        val array1 = JArray()
        val value3 = JValue(true)
        val object2 = JObject()
        array1.addJSON(value3)
        array1.addJSON(object2)

        val object3 = JObject()

        object1.addJSON("value1" , value1)
        object1.addJSON("value2" , value2)
        object1.addJSON("array1", array1)
        object1.addJSON("object3", object3)

        return object1
    }

    @Test
    fun serializeTest(){

        val serializer = Serializer(File("C:\\Users\\antun\\Desktop\\a.txt"))
        val obj = testObject()
        obj.accept(serializer)
        //print(serializer.getFullTextSerialized())
        val s = "{ \n\t\"value1\": \"a\",\n\t\"value2\": \"11\",\n\t\"array1\": [ \n\t\t\"true\",\n\t\t{ \n\t\t}\n\t],\n\t\"object3\": { \n\t}\n}"
        assertEquals(s,serializer.getFullTextSerialized())

    }

    @Test
    fun converterTest(){
        val serializer = Serializer(File("C:\\Users\\antun\\Desktop\\a.txt"))
        val serializer2 = Serializer(File("C:\\Users\\antun\\Desktop\\a.txt"))
        val comms = Comms(HashMap<String, JSONType>())

        val object1 = JObject()
        val value1 = JValue("a")
        val array1 = JArray()
        object1.addJSON("array", array1)
        object1.addJSON("string", value1)
        object1.accept(serializer)


        val string1:String = "a"
        val array2 = arrayListOf<Any>()
        val dt = DataClass(string1,array2)
        comms.mapObjects(dt).accept(serializer2)



        assertEquals(serializer.getFullTextSerialized(),serializer2.getFullTextSerialized())








    }


}