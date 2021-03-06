#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include "backend.h"
#include "engine.h"

int main(int argc, char *argv[])
{
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);

    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;
    const QUrl url(QStringLiteral("qrc:/main.qml"));
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
                     &app, [url](QObject *obj, const QUrl &objUrl) {
        if (!obj && url == objUrl)
            QCoreApplication::exit(-1);
    }, Qt::QueuedConnection);

    // Registering new QML types here
    qmlRegisterType<BackEnd>("custom.classes", 1, 0, "BackEnd");
    qmlRegisterType<Engine>("custom.classes", 1, 0, "Engine");

    engine.load(url);

    return app.exec();
}
