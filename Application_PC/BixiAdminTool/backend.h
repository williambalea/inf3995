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
    Q_PROPERTY(bool attemps MEMBER attemps NOTIFY attempsChanged)
    Q_PROPERTY(QString sqlData READ sqlData WRITE setSqlData NOTIFY sqlDataChanged)
    Q_PROPERTY(bool isLoggedIn READ isLoggedIn NOTIFY isLoggedInChanged)
    Q_PROPERTY(QString user READ user WRITE setUser NOTIFY userChanged)
    Q_PROPERTY(QString pass READ user WRITE setPass NOTIFY passChanged)
    QML_ELEMENT
public:
    explicit BackEnd(QObject *parent = nullptr);

    QString sqlData();
    bool isLoggedIn();
    QString user();
    QString pass();
    void setUser(const QString &data);
    void setPass(const QString &data);
    void setSqlData(const QString &data);

    Q_INVOKABLE void refresh();
    Q_INVOKABLE void login();

signals:
    void attempsChanged();
    void sqlDataChanged();
    void isLoggedInChanged();
    void userChanged();
    void passChanged();

private slots:
    void sqlFinished(QNetworkReply *reply);
    void loginFinished(QNetworkReply *reply);

private:
    QNetworkRequest makeRequest(const QUrl &url);

    QString m_sqlData = "";
    QString m_user = "";
    QString m_pass = "";
    QNetworkAccessManager *man;
    bool m_isLoggedIn = false;
    bool attemps = false;

};

#endif // BACKEND_H
