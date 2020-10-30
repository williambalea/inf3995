import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

ApplicationWindow {
    id: applicationWindow
    width: 1000
    height: 600
    visible: true
    color: "#9b0000"
    title: qsTr("Bixi Admin Tool")
    Material.accent: "#D52B1E"

    BackEnd {
        id: backend
    }

    StackView {
        id: stackView
        anchors.fill: parent
        initialItem: "App.qml"
    }
}




