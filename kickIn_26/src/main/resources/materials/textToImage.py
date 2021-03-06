from PIL import Image, ImageDraw
import base64
import json

def main():
    file = open("Active-material-types.csv", "r")
    jsonDumps = {"fileImages":[]}
    for item in file:
        data = item.split(',')
        picId = data[0]
        name = data[1]
        description = data[2][:-1]

        stringToPutIntoImage, heightMultiplier = prepareString(name)
        width = 10 * len(name)
        height = 15 * heightMultiplier

        img = Image.new('RGB', (1000, 1000), color=(255, 255, 255))
        drawing = ImageDraw.Draw(img)

        textW, textH = drawing.textsize(stringToPutIntoImage)
        img = Image.new('RGB', (textW, textH), color=(255, 255, 255))
        drawing = ImageDraw.Draw(img)
        drawing.text((0,0), stringToPutIntoImage, fill='black')

        img = img.convert("RGBA")
        imgData = img.getdata()

        newData = []
        for item in imgData:
            if item[0] == 255 and item[1] == 255 and item[2] == 255:
                newData.append((255, 255, 255, 0))
            else:
                newData.append(item)

        img.putdata(newData)
        img.save('D:\Documents\Downloads\materials\pictures\\' + picId + '.png', 'PNG')
        url = base64.b64encode(open('D:\Documents\Downloads\materials\pictures\\' + picId + '.png', 'rb').read())
        jsonDumps['fileImages'].append({'id': int(picId), 'name': name, 'description': description, 'url': str(url)})
    file.close()

    with open('data.json', 'w', encoding='utf-8') as file:
        json.dump(jsonDumps, file, ensure_ascii=False, indent=4)


def prepareString(text):
    stringToReturn = ''
    heightMultiplier = 1
    for letter in text:
        if letter == '(':
            stringToReturn += '\n('
            heightMultiplier += 1
        else:
            stringToReturn += letter
    return stringToReturn, heightMultiplier

main()
