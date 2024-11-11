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
