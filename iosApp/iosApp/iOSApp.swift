import SwiftUI
import ComposeApp
import Combine
import UIKit

@main
struct iOSApp: App {
    @StateObject private var tokenHolder = TokenHolder()
    private var mainViewController: UIViewController
    private var window: UIWindow?

    init() {
        mainViewController = MainViewControllerKt.MainViewController(token: nil)
        let navigationController = UINavigationController(rootViewController: mainViewController)
        InitKoinKt.InitKoin(viewController: navigationController)

        window = UIWindow(frame: UIScreen.main.bounds)
        window?.rootViewController = navigationController
        window?.makeKeyAndVisible()
    }

    var body: some Scene {
        WindowGroup {
            ContentView(tokenHolder: tokenHolder)
                .onOpenURL { url in
                    handleDeepLink(url: url)
                }
        }
    }

    private func handleDeepLink(url: URL) {
        if url.scheme == "dashpivot" && url.host == "verify" {
            let token = url.queryParameters?["token"]
            print("iOSApp: Token received from deep link: \(token ?? "no token")")
            tokenHolder.token = token
            TokenUpdateKt.tokenUpdate(token: token)
        }
    }
}

extension URL {
    var queryParameters: [String: String]? {
        var params = [String: String]()
        URLComponents(url: self, resolvingAgainstBaseURL: false)?.queryItems?.forEach {
            params[$0.name] = $0.value
        }
        return params
    }
}
