jpackage \
  --input target \
  --name "XlsPdfMaker1" \
  --main-jar "XlsPdfMaker1.jar" \
  --main-class com.example.AppStart \
  --type dmg \
  --icon src/main/resources/MyIcon.icns

sips -z 16 16 MyIcon.iconset/icon_16x16.png
sips -z 16 16 app_icon.png --out MyIcon.iconset/icon_16x16.png
//собрать иконки
iconutil -c icns MyIcon.iconset


jpackage --input target --name "XlsPdfMaker1" --main-jar "XlsPdfMaker1.jar" --main-class AppStart --type app-image
