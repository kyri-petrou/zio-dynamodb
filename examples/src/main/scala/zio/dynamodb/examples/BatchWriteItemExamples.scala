package zio.dynamodb.examples

import zio.dynamodb.DynamoDBExecutor.TestData.tableName1
import zio.dynamodb.DynamoDBQuery.{ deleteItem, putItem }
import zio.dynamodb.ProjectionExpression.$
import zio.dynamodb._

class BatchWriteItemExamples {
  val pk1    = PrimaryKey(Map("field1" -> AttributeValue.Number(1.0)))
  val item1  = Item(Map("field1" -> AttributeValue.Number(1.0)))
  val item2  = Item(Map("field2" -> AttributeValue.Number(2.0)))
  val table1 = TableName("T1")
  val table2 = TableName("T2")

  val batchManual =
    (putItem(table1, Item(Map("field1" -> AttributeValue.Number(1.0)))) where $("a.b") === "1") <*> deleteItem(
      table2,
      pk1
    ) where $("c.b") === "2"

  val batchPutFromIterable = DynamoDBQuery.forEach(1 to 3) { i =>
    putItem(table1, Item(Map("field1" -> AttributeValue.String(i.toString))))
  }

  val batchDeleteFromIterable = DynamoDBQuery.forEach(1 to 3) { i =>
    deleteItem(tableName1, PrimaryKey(Map("pk" -> AttributeValue.String(i.toString)))) where $("foo.bar") > "1" && !($(
      "foo.bar"
    ) < "5")
  }

}
