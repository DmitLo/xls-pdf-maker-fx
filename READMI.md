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


//mac
jpackage --input target --name "XlsPdfMaker1" --main-jar "XlsPdfMaker1.jar" --main-class AppStart --type app-image

//windows
jpackage --type exe --input target --dest output --name "XlsPdfMaker1" --main-jar XlsPdfMaker0-1-6.jar --win-shortcut --win-menu
jpackage --type msi --input target --name "xls-pdf-maker1" --main-jar xlspdfmaker0-1-6.jar --win-per-user-install --win-dir-chooser --win-shortcut --win-menu --win-menu-group "TechL" --icon src/main/resources/myicon.ico

//установк конвертера из icns в ico
winget install -e --id Gyan.FFmpeg --exact
ffmpeg -i icon.icns icon.ico
ffmpeg -i src/main/resources/myicon.icns src/main/resources/myicon.ico

jpackage --type app-image \
--name "XlsPdfMaker1" \
--input target \
--main-jar XlsPdfMaker1.jar \
--java-options "-Dapple.laf.useScreenMenuBar=true"
