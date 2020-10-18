#ifndef BACKEND_H
#define BACKEND_H

#include <QObject>
#include <QString>
#include <qqml.h>
#include <QNetworkAccessManager>
#include <QNetworkReply>

class BackEnd : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString userName READ userName WRITE setuserName NOTIFY userNameChanged)
    Q_PROPERTY(QString sqlData READ sqlData WRITE setSqlData NOTIFY sqlDataChanged)
    QML_ELEMENT
public:
    explicit BackEnd(QObject *parent = nullptr);

    QString userName();
    void setuserName(const QString &userName);

    QString sqlData();
    void setSqlData(const QString &data);

    Q_INVOKABLE void refresh();

signals:
    void userNameChanged();
    void sqlDataChanged();

private slots:
    void sqlFinished(QNetworkReply *reply);

private:
    QString m_userName;
    QString m_sqlData = "";
};

#endif // BACKEND_H
