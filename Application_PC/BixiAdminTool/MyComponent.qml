import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Rectangle {
    width:100
    height:50
    color:"lightblue"
    property string name: "";
    signal dropped(int x,int y,string name);
}
