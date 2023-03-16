def marshal(message: map) -> bytearray:
    res = bytearray()

    for field in message:
        joiner = []
        
        joiner.append(field)

        if isinstance(message[field], int):
            fieldType = "Integer"
            content = str(message[field])
        elif isinstance(message[field], str):
            fieldType = "String"
            content = str(message[field])
        elif isinstance(message[field], float):
            fieldType = "Float"
            content = str(message[field])
        elif isinstance(message[field], list):
            if isinstance(message[field][0], int):
                fieldType = "IntegerArray"
                content = "]]]".join([str(i) for i in message[field]])
            elif isinstance(message[field][0], str):
                fieldType = "StringArray"
                content = "]]]".join(message[field])
        joiner.append(fieldType)
        joiner.append(content)

        joinedString = "@@@".join(joiner)
        tempBuf = bytearray()
        tempBuf.extend(joinedString.encode())
        byteSize = len(tempBuf)
        
        res.extend(byteSize.to_bytes(4, byteorder="big"))
        res.extend(tempBuf)

    return res

def unmarshal(message: bytearray) -> map:
    res = {}

    pointer = 0
    while pointer < len(message):
        messageSize = message[pointer: pointer+4]
        pointer += 4
        value = int.from_bytes(messageSize, "big")
        
        if value == 0:
            break
        
        temp = message[pointer: pointer + value]
        combined = temp.decode()
        pointer += value

        field, fieldType, content = combined.split("@@@")

        if fieldType == "Integer":
            res[field] = int(content)
        elif fieldType == "Float":
            res[field] = float(content)
        elif fieldType == "String":
            res[field] = content
        elif fieldType == "IntegerArray":
            res[field] = [int(i) for i in content.split("]]]")]
        elif fieldType == "StringArray":
            res[field] = content.split("]]]")
    
    return res


""" This segment is just for testing the Marshaller and Unmarshaller. To be deleted later on """
if __name__ == "__main__":
    testmap = {}
    testmap["Flight Number"] = 123
    testmap["Message"] = "Long flight message details here"
    testmap["Cost"] = 0.54
    testmap["Sample flight list"] = [1,2,3,4]
    testmap["Sample destinations"] = ["Singapore", "Hong Kong"]

    res = marshal(testmap)
    message = unmarshal(res)
    for key in message:
        print(key, message[key])